<?php

require_once('init.php');
require_once(__LIBS__ . 'gcm.class.php');
require_once(__LIBS__ . 'MCrypt.class.php');
require_once(__TABLES__ . 'Devices.php');

$Devices  = new Devices();
$Data = getData('data');

$action = getData('action');
$deviceid = getData('deviceid');
$tokenid = getData('tokenid');
$programVersion = getData('programversion');
$licenseType = getData('licensetype');

switch ($action) {
	case 'register' :
		if($deviceid !== '') {
			$Devices->DeviceID = $deviceid;
			$Devices->TokenID = $tokenid;
			if($programVersion !== '') {
			  $Devices->ProgramVersion = $programVersion;
			}
			if($licenseType !== '') {
				$Devices->LicenseType = $licenseType;
			}
			$Devices->LastCheck = date("Y-m-d", time());
			$ExistDevice = $Devices->find(array('DeviceID' => $deviceid));
			if(count($ExistDevice) > 0) {
				$Devices->_id = $ExistDevice['_id'];
				$Devices->Save();
			} else {
				$Devices->Create();
			}
		}
		break;
	case 'msg' :
		$deviceid = filter_input(INPUT_POST, 'deviceid');
		$title = filter_input(INPUT_POST, 'title');
		$msg = filter_input(INPUT_POST, 'msg');
		$url = filter_input(INPUT_POST, 'url');
		$ExistDevice = $Devices->find(array('DeviceID' => $deviceid));
			if(count($ExistDevice) > 0) {
				$tokenid = $ExistDevice['TokenID'];
				$GCM = new Gcm();
				echo $GCM->SendMsg($tokenid, $title, $msg, $url);
			}
		break;
	default :
		if ($Data == "") {
			$allDevice = $Devices->all();
			echo '<table><tr><td>_id</td><td>Device</td><td>Token</td><td>LastChk</td></tr>';
			foreach ($allDevice as $key => $value) {
				echo '<tr><td>' . $value['_id'] . '</td><td>' . $value['DeviceID'] . '</td><td>' . $value['TokenID'] . '</td><td>' . $value['LastCheck'] . '</td></tr>';
			}
			echo '</table>';
		} else {
			chkLicence();
		}
		break;
}

function chkLicence() {
	global $Data, $Devices;
    $DataFromBase64 = base64_decode($Data);
    $mcrypt = new MCrypt();
    if ($Data == "DEBUG") {
        $decrypted = "{\"programversion\":\"1.0.3\",\"deviceid\":\"DEBUG-503b-d60b-ffff-ffffef05ac4a\"}";
    } else {
        $decrypted = $mcrypt->decrypt($DataFromBase64);
    }
		$getData = json_decode($decrypted, true);
		$ExistDevice = $Devices->find(array('DeviceID' => $getData['deviceid'] ));
		if(count($ExistDevice) > 0) {
			$Devices->_id = $ExistDevice['_id'];
			$Devices->ProgramVersion = isset($getData['programversion']) ? $getData['programversion'] : '';
			$Devices->Url = isset($getData['url']) ? $getData['url'] : '';
			if($ExistDevice['LicenseDate']=='') {
				$Devices->LicenseDate = max("2015-12-20", date("Y-m-d", time() + 60 * 60 * 24 * 30));
			}
			if($ExistDevice['LicenseType']=='') {
				$Devices->LicenseType = 'free';
			}
			if($ExistDevice['LicenseVersion']=='') {
				$Devices->LicenseVersion = 'any';
			}
			$Devices->LastCheck = date("Y-m-d", time());
			$Devices->Save();
		} else {
			$Devices->DeviceID = $getData['deviceid'];
			$Devices->LicenseDate = max("2015-12-20", date("Y-m-d", time() + 60 * 60 * 24 * 30));
			$Devices->LicenseType = 'free';
			$Devices->LicenseVersion = 'any';
			$Devices->ProgramVersion = isset($getData['programversion']) ? $getData['programversion'] : '';
			$Devices->Url = isset($getData['url']) ? $getData['url'] : '';
			$Devices->LastCheck = date("Y-m-d", time());
			$Devices->Create();
		}

		$respData = $Devices->find(array('DeviceID' => $getData['deviceid'] ));
		$jData['licensetype'] = $respData['LicenseType'];
		$jData['licensedate'] = $respData['LicenseDate'];
		$jData['licenseversion'] = $respData['LicenseVersion'];


		$resp = $mcrypt->encrypt(json_encode($jData));
		if ($Data == "DEBUG") {
			echo "Resp: " . json_encode($jData) . '<br />';
    } else {
        echo base64_encode($resp);
    }
}

function getData($key) {
    $return = filter_input(INPUT_POST, $key, FILTER_DEFAULT);
    if ($return == "") {
        $return = filter_input(INPUT_GET, $key, FILTER_DEFAULT);
    }
    return $return;
}

