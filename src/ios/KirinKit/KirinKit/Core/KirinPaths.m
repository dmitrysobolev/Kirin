//
//  KirinPaths.m
//  KirinKit
//
//  Created by James Hugman on 11/01/2012.
//  Copyright 2012 Future Platforms. All rights reserved.
//

#import "KirinPaths.h"


@implementation KirinPaths


+ (NSString*) indexFilename {
	return @"Kirin.html";
}

+ (NSString*) javascriptDirectory {
	return @"app";
}

+ (NSString*) join: (NSString*) areaPath andFilePath: (NSString*) filePath {
    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[areaPath componentsSeparatedByString:@"/"]];
    [directoryParts addObjectsFromArray:[filePath componentsSeparatedByString:@"/"]];

    // TODO make sure we can put in leading paths or not.
    
    return [directoryParts componentsJoinedByString:@"/"];
}

+ (NSString*) pathForResource:(NSString*)resourcepath {
    NSBundle * mainBundle = [NSBundle mainBundle];
    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[resourcepath componentsSeparatedByString:@"/"]];
    NSString       *filename       = [directoryParts lastObject];
    [directoryParts removeLastObject];	
    NSString *directoryStr = [KirinPaths join: [self javascriptDirectory] andFilePath:[directoryParts componentsJoinedByString:@"/"]];

    return [mainBundle pathForResource:filename ofType:@"" inDirectory:directoryStr];
}

+ (NSString*) filePathInAppAssets: (NSString*) path {
    return [KirinPaths pathForResource:path];
}

+ (NSString*) fileInJavascriptDir: (NSString*) path {
    return [KirinPaths pathForResource:path];
}

+ (NSString*) filePathInTempDir: (NSString*) path {
    // cleaned out automatically every 3 days but otherwise persists between application launches and reboots
    return [KirinPaths join:NSTemporaryDirectory() andFilePath:path];
}

+ (NSString*) filePathInDocuments: (NSString*) path {
    NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, 
                                                   NSUserDomainMask, YES);
    return [KirinPaths join:[dirPaths objectAtIndex:0] andFilePath:path];
}


@end
