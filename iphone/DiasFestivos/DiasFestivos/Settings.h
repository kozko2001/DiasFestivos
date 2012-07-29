//
//  Settings.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 25/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Settings : NSObject
{
    NSUserDefaults* defaults;
}

-(NSString*) getComunidad;
-(NSString*) getProvincia;
-(NSString*) getLocalidad;

-(void) setComunidad:(NSString*) c;
-(void) setProvincia:(NSString*) c;
-(void) setLocalidad:(NSString*) c;

-(BOOL) isSetted;
@end
