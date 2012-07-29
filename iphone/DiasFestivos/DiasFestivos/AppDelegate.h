//
//  AppDelegate.h
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@class ViewController;
@class Settings;
@class Localidades;
@class DiasFestivos;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UIViewController *viewController;
@property (strong, nonatomic) Localidades*  localidades_model;
@property (strong, nonatomic) DiasFestivos*  dias_model;
@property (strong, nonatomic) Settings*     settings;

-(void) showCalendar;
@end
