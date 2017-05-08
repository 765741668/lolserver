@echo on
set configPath=%1%

for /f "eol=;tokens=2,2 delims==" %%i in ('findstr /i "protobuf.location" %configPath%') do set location=%%i
for /f "eol=;tokens=2,2 delims==" %%i in ('findstr /i "protobuf.source.path" %configPath%') do set sourcePath=%%i
for /f "eol=;tokens=2,2 delims==" %%i in ('findstr /i "protobuf.output" %configPath%') do set output=%%i
for /f "eol=;tokens=2,2 delims==" %%i in ('findstr /i "protobuf.source.files" %configPath%') do set files=%%i

for %%a in (%configPath%) do (
 for /f "usebackq delims=" %%b in ("%%a") do (
 	for %%c in (%%b) do (
 		echo %%c | find ".proto" && %location:"=%\protoc %sourcePath:"=%\%%c --java_out=%output:"=%
 	)
 )
 goto :end
)
:end

echo generate java sources from protobuf done!

