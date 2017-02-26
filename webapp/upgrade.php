<?php

require_once('init.php');
require_once(__TABLES__ . 'Devices.php');

$Devices  = new Devices();

$DirList = scandir('.');
$LicencNumbers = 0;
foreach ($DirList as $File) {
	if (preg_match('/^[0-9a-f]{8}/',$File)) {
		echo 'License: ' . $File;
		$getData = json_decode(file_get_contents($File), true);
		$ExistDevice = $Devices->find(array('DeviceID' => $File));
		$Devices->DeviceID = $File;
		$Devices->LicenseDate = $getData['licensedate'];
		$Devices->LicenseType = $getData['licensetype'];
		$Devices->LicenseVersion = isset($getData['licenseversion']) ? $getData['licenseversion'] : '';
		$Devices->ProgramVersion = isset($getData['programversion']) ? $getData['programversion'] : '';
		$Devices->Url = isset($getData['url']) ? $getData['url'] : '';
		if(count($ExistDevice) > 0) {
			$Devices->Save();
			echo ' saved.<br />';
		} else {
			$Devices->Create();
			echo ' created.<br />';
		}
	}
}

