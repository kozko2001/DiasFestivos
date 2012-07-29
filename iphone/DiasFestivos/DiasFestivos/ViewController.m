//
//  ViewController.m
//  DiasFestivos
//
//  Created by Jordi Coscolla on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"
#import "DiasFestivos.h"
#import "DiasFestivosLoader.h"
#import "Settings.h"

@interface ViewController ()
{    
@private
    AppDelegate *app;
    Settings    *settings;
    DiasFestivos *model;
    NSArray      *currentModel;
}

@end
@implementation ViewController
@synthesize table;
@synthesize optionsButton;

- (void)viewDidLoad
{
    app = (AppDelegate*) [[UIApplication sharedApplication] delegate];    
    [super viewDidLoad];

    settings = [app settings];
    model = [app dias_model];
    
    [[[DiasFestivosLoader alloc] init] 
     request: model 
     WithLocalidad: [settings getLocalidad]
     WithTarget:self
     WithOnComplete:@selector(onLoaderComplete) 
     WithOnError:@selector(onLoaderError)];
    
}

- (void)viewDidUnload
{
    [self setTable:nil];
    [self setOptionsButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"DiaCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"DiaCell" owner:self options:nil];
        cell = (UITableViewCell *)[nib objectAtIndex:0];
    }
    DiasFestivosData *dia = [model.data objectAtIndex: [indexPath row]];
    
    UILabel *l_nombre = (UILabel*) [ cell viewWithTag:20];
    UILabel *l_fecha =  (UILabel*) [ cell viewWithTag:5];    
    UILabel *l_dias = (UILabel*) [cell viewWithTag: 10];
    [l_nombre setText:dia.nombreFiesta];
    [l_fecha setText: dia.fecha];
    if( dia.diasRestantes >= 0 )
        [l_dias setText: [NSString stringWithFormat:@"%i" , dia.diasRestantes]];
    else
        [l_dias setText: @"-"];
    
    return cell;

}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if( model.data == nil) 
        return 0;
    
    return [model.data count];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
    } else {
        return YES;
    }
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
//    [table deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 67;
}


#pragma mark - Loader events

-(void) onLoaderComplete{
    [table reloadData];
}

-(void) onLoaderError{
        
}

@end
