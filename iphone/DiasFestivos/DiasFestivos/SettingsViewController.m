//
//  SettingsViewController.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 25/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "SettingsViewController.h"
#import "Localidades.h"
#import "LocalidadesLoader.h"
#import "Settings.h"
#import "AppDelegate.h"

@interface SettingsViewController ()
{    
    @private
    AppDelegate *app;
    Settings    *settings;
    Localidades *model;
    NSArray     *currentModel;
}
@end


@implementation SettingsViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    app = (AppDelegate*) [[UIApplication sharedApplication] delegate]; 
    settings = [app settings];
    model = [app localidades_model];
    
    [[[LocalidadesLoader alloc] init] 
     request: app.localidades_model
     WithTarget:self
     WithOnComplete:@selector(onLocalidadesLoaderComplete) 
     WithOnError:@selector(onLocalidadesLoaderError)];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [currentModel count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"LocalidadCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    LocalidadesData *data = (LocalidadesData*) [currentModel objectAtIndex: [indexPath row]];
    
    cell.textLabel.text = data.name;
    
    
    return cell;
}

-(void) createNewCurrentModel
{
    if([settings getComunidad] == nil)
        currentModel= [model GetComunidades];
    else if([settings getProvincia] == nil)
        currentModel= [model GetProvinciasWithComunidad: [settings getComunidad]];
    else if([settings getLocalidad] == nil)
        currentModel= [model 
                       GetLocalidadesWithComunidad: [settings getComunidad] 
                       AndWithProvincia:[settings getProvincia]];
}

-(void) onLocalidadesLoaderComplete{
    [self createNewCurrentModel];
    
    [[self tableView] reloadData];
}

-(void) onLocalidadesLoaderError{
    
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    LocalidadesData *data = (LocalidadesData*) [currentModel objectAtIndex: [indexPath row]];
    
    if([settings getComunidad] == nil)
        [settings setComunidad: [data code]];
    else if([settings getProvincia] == nil)
        [settings setProvincia: [data code]];
    else if([settings getLocalidad] == nil)
    {
        [settings setLocalidad: [data code]];
        [app showCalendar];
        return;
    }
    [self createNewCurrentModel];
    [[self tableView] reloadData];
}

@end
