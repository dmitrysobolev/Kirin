cd "src/ios/KirinKit"
xcodebuild -sdk iphoneos -project KirinKit.xcodeproj -target KirinKit -configuration Debug -verbose clean build
xcodebuild -sdk iphoneos -project KirinKit.xcodeproj -target KirinKit -configuration Release -verbose clean build