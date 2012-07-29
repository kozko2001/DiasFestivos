//
//  DiasFestivosLoader.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
@class  DiasFestivos;

@interface DiasFestivosLoader : NSObject
-(void) request: (DiasFestivos*) model
  WithLocalidad: (NSString*) localidad 
     WithTarget: (id)target 
 WithOnComplete: (SEL) onComplete
    WithOnError: (SEL) onError;

@end
