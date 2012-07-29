//
//  Localidades.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Localidades : NSObject
{
    NSArray* data;
}
-(void) fill:(NSArray*)a;

-(NSArray*) GetComunidades;
-(NSArray*) GetProvinciasWithComunidad: (NSString*)c ;
-(NSArray*) GetLocalidadesWithComunidad: (NSString*)c AndWithProvincia: (NSString*)x;

@end

@interface LocalidadesData:NSObject
    @property (strong, nonatomic)  NSString* name;
    @property (strong, nonatomic)  NSString* code;
    @property (strong, nonatomic)  NSString* parent_code;
    @property (assign, nonatomic)  int       level;

-(id) initWithName: (NSString*) name
          WithCode: (NSString*) code 
    WithParentCode: (NSString*) parentCode
         WithLevel: (int) level ;
@end
