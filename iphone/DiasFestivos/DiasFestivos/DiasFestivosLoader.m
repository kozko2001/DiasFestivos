//
//  LocalidadesLoader.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "DiasFestivosLoader.h"
#import "AFJSONRequestOperation.h"
#import "Constants.h"
#import "DiasFestivos.h"
#import <CoreData/CoreData.h> 

@interface DiasFestivosLoader ()
{
    NSManagedObjectContext *coredata_ctx;
    NSEntityDescription *entity;
}

@end

@implementation DiasFestivosLoader

-(void) request: (DiasFestivos*) model
  WithLocalidad: (NSString*) localidad
coredataContext: (NSManagedObjectContext*) ctx
     WithTarget: (id)target  
 WithOnComplete: (SEL) onComplete
    WithOnError: (SEL) onError
{
    coredata_ctx = ctx;
    entity = [NSEntityDescription entityForName:@"DiaFestivo" inManagedObjectContext:coredata_ctx];

    if( ! [self haveCacheData] ) 
        [self loadUsingJson: model WithLocalidad:localidad
                 WithTarget:target WithOnComplete:onComplete WithOnError:onError];
    else
        [self loadUsingCoreData: model 
                     WithTarget:target WithOnComplete:onComplete WithOnError:onError];
    
}

-(BOOL) haveCacheData
{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:entity];
    NSError *error;
    
    NSArray *countForFetchRequest = [coredata_ctx executeFetchRequest:fetchRequest error: &error];
    return !( countForFetchRequest == nil || [countForFetchRequest count] == 0);
}

#pragma mark - CoreData loader...

-(void) loadUsingCoreData: (DiasFestivos*) model
         WithTarget: (id)target  
         WithOnComplete: (SEL) onComplete
         WithOnError: (SEL) onError
{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:entity];
    NSError *error;
    
    NSMutableArray *data = [[NSMutableArray alloc] init];
    
    NSArray *fetchedObjects = [coredata_ctx executeFetchRequest:fetchRequest error:&error];
    for (NSManagedObject *info in fetchedObjects) {
        NSNumber* dia = [info valueForKey: @"dia"];
        NSNumber* mes = [info valueForKey: @"mes"];
        NSNumber* ano = [info valueForKey: @"ano"];
        NSString *nombre = [info valueForKey: @"nombre"];
        NSString *dia_semana = [info valueForKey: @"dia_semana"];   
        
        [self CreateEntity:data 
                   WithDia: [dia intValue]
                   WithMes: [mes intValue]
                   WithAno: [ano intValue]
                  WithName:nombre
             WithDiaSemana:dia_semana];
        
    }
    model.data = data;
}


-(void) saveToCoreDataWithDia:(NSInteger) dia 
                      WithMes: (NSInteger) mes
                      WithAno: (NSInteger) ano 
                     WithName: (NSString*) nombre
                WithDiaSemana: (NSString*) diaSemana 
{
    NSManagedObject *dia_festivo = [NSEntityDescription
                                       insertNewObjectForEntityForName:@"DiaFestivo"
                                       inManagedObjectContext:coredata_ctx];
    [dia_festivo setValue:[NSNumber numberWithInt:dia] forKey:@"dia"];
    [dia_festivo setValue:[NSNumber numberWithInt:mes] forKey:@"mes"];
    [dia_festivo setValue:[NSNumber numberWithInt:ano] forKey:@"ano"];
    [dia_festivo setValue:nombre  forKey:@"nombre"];
    [dia_festivo setValue:diaSemana forKey:@"dia_semana"];
    
    NSError *error;
    if (![coredata_ctx save:&error]) {
        NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
    }
}

#pragma mark - Internet & Json loader functions...

-(void) loadUsingJson: (DiasFestivos*) model
        WithLocalidad: (NSString*) localidad 
           WithTarget: (id)target  
       WithOnComplete: (SEL) onComplete
          WithOnError: (SEL) onError
{
    NSString *construct_url = [[NSString alloc] initWithFormat: @"%@/%@.json", WS_BASE, localidad];
    
    NSURL *url = [NSURL URLWithString: construct_url];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
        [self parseWithJson: JSON AndWithModel: model];
        
        
        [target performSelector: onComplete];
    } failure: ^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON){
        NSLog(@"%@", [error localizedDescription]);
        [target performSelector: onError];
    }];
    [operation start];
}
-(void) parseWithJson:(id)JSON AndWithModel: (DiasFestivos*) model
{
    /*
     "dia_semana": "Viernes", 
     "ano": "2012", 
     "month": "01", 
     "nombre": "\u00a0Epifan\u00eda del Se\u00f1or", 
     "type": "diaFiestaNacional", 
     "day": "6"

     */
    NSArray* json_array = (NSArray*) JSON;
    NSMutableArray *data = [[NSMutableArray alloc] init];
    
    for (NSDictionary* dia_json in json_array) {
        NSString *dia_semana = [dia_json objectForKey: @"dia_semana"];
        NSInteger ano = [[dia_json objectForKey: @"ano"] intValue];
        NSInteger month = [[dia_json objectForKey: @"month"] intValue];
        NSString *nombre = [dia_json objectForKey: @"nombre"];
        NSString *type = [dia_json objectForKey: @"type"];
        NSInteger day = [[dia_json objectForKey: @"day"] intValue];
        
        [self CreateEntity:data 
                   WithDia:day
                   WithMes:month
                   WithAno:ano
                  WithName:nombre
             WithDiaSemana:dia_semana];
       
        [self saveToCoreDataWithDia:day WithMes:month WithAno:ano 
                           WithName:nombre WithDiaSemana:dia_semana];
    }
    model.data = data;

}

#pragma mark - Shared functions

-(void) CreateEntity: (NSMutableArray*) data
             WithDia:(NSInteger) dia 
             WithMes: (NSInteger) mes
             WithAno: (NSInteger) ano 
            WithName: (NSString*) nombre
       WithDiaSemana: (NSString*) diaSemana
{
    NSDateComponents *components = [[NSDateComponents alloc] init];
    [components setDay: dia];
    [components setMonth: mes];
    [components setYear: ano];
    
    NSCalendar *sysCalendar = [NSCalendar currentCalendar];
    NSDate *date = [sysCalendar dateFromComponents:components];
    
    
    NSDate *todaysDate = [NSDate date];
    NSTimeInterval lastDiff = [date timeIntervalSinceNow];
    NSTimeInterval todaysDiff = [todaysDate timeIntervalSinceNow];
    NSTimeInterval dateDiff = lastDiff - todaysDiff;
    
    
    DiasFestivosData *model = [[DiasFestivosData alloc] init];
    model.fecha = [NSString stringWithFormat: @"(%@) %i %i %i", diaSemana, dia, mes, ano];
    model.nombreFiesta = nombre;
    model.diasRestantes = (dateDiff / (60*60*24)) +1;
    
    if(model.diasRestantes >= 0 ) 
        [data addObject: model]; 
}


@end
