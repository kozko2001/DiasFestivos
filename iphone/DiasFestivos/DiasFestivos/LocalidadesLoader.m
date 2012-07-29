//
//  LocalidadesLoader.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "LocalidadesLoader.h"
#import "AFJSONRequestOperation.h"
#import "Constants.h"

@implementation LocalidadesLoader

-(void) request: (Localidades*) model WithTarget: (id)target WithOnComplete: (SEL) onComplete WithOnError: (SEL) onError
{
    NSString *construct_url = [[NSString alloc] initWithFormat: @"%@/%@", WS_BASE, @"comunidades.json"];
    
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

-(void) parseWithJson:(id)JSON WithParentCode:(NSString*) p_code WithLevel: (int) level
{
    NSArray *d = (NSArray*) JSON;
    for (NSDictionary* comunidad_json in d) {
        [self createLocalidadData: comunidad_json WithParentCode:p_code WithLevel:level];
        
        if( level == 0 || level == 1) 
            [self parseWithJson: [comunidad_json valueForKey: level==0?@"prov":@"local"]
                 WithParentCode: [comunidad_json valueForKey: @"code"] 
                      WithLevel: level+1];
    }

}

-(void) parseWithJson:(id)JSON AndWithModel: (Localidades*) model
{
    data = [[NSMutableArray alloc] init];
    [self parseWithJson:JSON WithParentCode: @"" WithLevel: 0];
    [model fill: data];
}

-(void*) createLocalidadData: (NSDictionary*) JSON
                         WithParentCode: (NSString*) parentCode 
                              WithLevel: (int) level
{
    NSString *name = [JSON valueForKey:@"name"];
    NSString *code = [JSON valueForKey:@"code"];
    NSLog(@"level : %i  parentCode: .%@." , level, parentCode);
    [data addObject: [[LocalidadesData alloc] initWithName:name 
                                        WithCode: code
                                  WithParentCode:parentCode 
                                       WithLevel:level]];
}
@end
