//
//  LocalidadesLoader.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Localidades.h"

@interface LocalidadesLoader : NSObject
{
    NSMutableArray *data;
}
-(void) request: (Localidades*) model WithTarget: (id)target WithOnComplete: (SEL) onComplete WithOnError: (SEL) onError;
@end
