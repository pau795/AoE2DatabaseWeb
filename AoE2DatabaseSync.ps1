#powershell.exe -command "& 'path'

#Directories paths

$dataDir = "D:\cosas\programacion random\Vaadin\aoe2-database-data"
$webDir = "D:\cosas\programacion random\Vaadin\aoe-2-database\src\main\resources\META-INF\resources\data"
$androidDir = "D:\cosas\programacion random\AoEIIDataBase\app\src\main\res"

$outputMsg ="";


#Copy to web
Copy-Item -Path "${dataDir}\*" -Destination "${webDir}" -Recurse -Force -Exclude "*.git*"

if($?) {
    $outputMsg += "Files successfully copied to AoE2DatabaseWeb."
}
else {
    $outputMsg += "Files could not be copied to Aoe2DatabaseWeb."
}

$outputMsg += "`n`n"


#Copy to android

#Copy-Item -Path "${dataDir}\*.xml" -Destination "${androidDir}\raw" -Recurse -Force
#Copy-Item -Path "${dataDir}\en\*.xml" -Destination "${androidDir}\values" -Recurse -Force
#Copy-Item -Path "${dataDir}\es\*.xml" -Destination "${androidDir}\values-es" -Recurse -Force
#Copy-Item -Path "${dataDir}\de\*.xml" -Destination "${androidDir}\values-de" -Recurse -Force


if($?) {
    $outputMsg += "Files successfully copied to AoE2DatabaseAndroid."
}
else {
    $outputMsg += "Files could not be copied to Aoe2DatabaseAndroid."
}


Add-Type -AssemblyName System.Windows.Forms
[System.Windows.Forms.MessageBox]::Show($outputMsg, 'Information')