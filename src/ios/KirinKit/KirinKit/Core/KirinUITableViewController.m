//
//  KirinUITableViewController.m
//  KirinKit
//
//  Created by James Hugman on 30/04/2012.
//  Copyright (c) 2012 Future Platforms. All rights reserved.
//

#import "KirinUITableViewController.h"
#import "Kirin.h"

@interface KirinUITableViewController ()

@end

@implementation KirinUITableViewController

@synthesize kirinHelper = kirinHelper_;

- (void) bindScreenWithoutLoading: (NSString*) moduleName {
    self.kirinHelper = [KIRIN bindScreen:self toModule:moduleName];
}

- (void) bindScreen:(NSString *)moduleName {
    [self bindScreenWithoutLoading:moduleName];
    [self.kirinHelper onLoad];
}

- (id) bindScreen:(NSString *)moduleName withProtocol: (Protocol*) protocol {
    [self bindScreen:moduleName];
    return [self.kirinHelper proxyForJavascriptObject:protocol];
}

- (void) viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self.kirinHelper onResume];
}

- (void) viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.kirinHelper onPause];
    
}

- (void)viewDidUnload
{
    [self.kirinHelper onUnload];
    
    [super viewDidUnload];
    self.kirinHelper = nil;
}


@end
