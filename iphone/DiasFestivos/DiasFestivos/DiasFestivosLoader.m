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

@implementation DiasFestivosLoader

-(void) request: (DiasFestivos*) model
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
        NSString *ano = [dia_json objectForKey: @"ano"];
        NSString *month = [dia_json objectForKey: @"month"];
        NSString *nombre = [dia_json objectForKey: @"nombre"];
        NSString *type = [dia_json objectForKey: @"type"];
        NSString *day = [dia_json objectForKey: @"day"];
        
        NSDateComponents *components = [[NSDateComponents alloc] init];
        [components setDay: [day intValue]];
        [components setMonth: [month intValue]];
        [components setYear: [ano intValue]];
        
        NSCalendar *sysCalendar = [NSCalendar currentCalendar];
        NSDate *date = [sysCalendar dateFromComponents:components];
        

        NSDate *todaysDate = [NSDate date];
        NSTimeInterval lastDiff = [date timeIntervalSinceNow];
        NSTimeInterval todaysDiff = [todaysDate timeIntervalSinceNow];
        NSTimeInterval dateDiff = lastDiff - todaysDiff;
        
        
        DiasFestivosData *dia = [[DiasFestivosData alloc] init];
        dia.fecha = [NSString stringWithFormat: @"(%@) %@ %@ %@", dia_semana, day, month, ano];
        dia.nombreFiesta = nombre;
        dia.diasRestantes = (dateDiff / (60*60*24)) +1;
        
        if(dia.diasRestantes >= 0 ) 
            [data addObject: dia]; 
    }
    model.data = data;

}

@end
