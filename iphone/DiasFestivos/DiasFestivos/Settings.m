//
//  Settings.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 25/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Settings.h"

@implementation Settings
    
- (id) init {
    self = [super init];
    if (self != nil) {
        defaults = [NSUserDefaults standardUserDefaults];
    }
    return self;
}


-(NSString*) getComunidad
{
    return [defaults valueForKey:@"comunidad"];
}

-(NSString*) getProvincia
{
    NSString *d= [defaults valueForKey:@"provincia"];
    NSLog(@"%@" , d );
    return d ;
}

-(NSString*) getLocalidad
{
    NSString *d =  [defaults valueForKey:@"localidad"];
    NSLog(@"%@" , d );
    return d;
}

-(void) setComunidad:(NSString*) c
{
    [defaults setValue:c forKey: @"comunidad"];
}

-(void) setProvincia:(NSString*) c
{
    [defaults setValue:c forKey: @"provincia"];
}

-(void) setLocalidad:(NSString*) c
{
    [defaults setValue:c forKey: @"localidad"];
}

-(BOOL) isSetted
{
    return [self getComunidad] != nil && 
           [self getProvincia] != nil &&
           [self getLocalidad] != nil;
}

@end
