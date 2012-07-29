//
//  DiasFestivos.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DiasFestivos : NSObject
@property (strong) NSArray* data;


@end

@interface DiasFestivosData : NSObject
@property (strong) NSString* nombreFiesta;
@property (assign) int diasRestantes;
@property (strong) NSString* fecha;
@property (strong) NSDate* date;

@end
