REM build the windows stuffs
mvn clean install -pl common/kirin-gwt -am
c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild wp8\wp8\KirinWP8\KirinWP8.csproj /t:Clean,Build
c:\windows\Microsoft.NET\Framework\v4.0.30319\MSBuild w8\w8\KirinW8\KirinW8.csproj /t:Clean,Build