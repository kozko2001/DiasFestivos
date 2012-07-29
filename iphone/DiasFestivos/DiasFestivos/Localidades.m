//
//  Localidades.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Localidades.h"
#import "LocalidadesLoader.h"
@implementation Localidades

-(void) fill:(NSArray*)a
{
    data = a;
}

-(NSArray*) GetComunidades
{
    return [data filteredArrayUsingPredicate:
            [NSPredicate predicateWithFormat:@"(level = 0)"]];

}
-(NSArray*) GetProvinciasWithComunidad: (NSString*)c 
{
    return [data filteredArrayUsingPredicate:
            [NSPredicate predicateWithFormat:@"(level = 1 AND parent_code like[c] %@)", c]];
}
-(NSArray*) GetLocalidadesWithComunidad: (NSString*)c AndWithProvincia: (NSString*)x
{
    return [data filteredArrayUsingPredicate:
            [NSPredicate predicateWithFormat:@"(level = 2 AND parent_code like[c] %@)", x]];
}



@end


@implementation LocalidadesData
@synthesize name, code, parent_code, level;

-(id) initWithName: (NSString*) name
          WithCode: (NSString*) code 
    WithParentCode: (NSString*) parentCode
         WithLevel: (int) level 
{
    LocalidadesData*  d = [self init];
    if( d != nil)
    {
        d.name  = name;
        d.code  = code;
        d.parent_code = parentCode;
        d.level = level;
    }
    return d;
}


@end