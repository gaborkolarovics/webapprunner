<?php

class Gcm {

	// API access key from Google API's Console
	//define('API_ACCESS_KEY', );
	private $headers;
	
	public function __construct() 
	{
		$this->headers = array (
			'Authorization: key=' . 'AIzaSyAhqHfL7vXR-WzwaCoaM2gLLtEGgDE-PNo' ,
			'Content-Type: application/json'
		);
	}
	
	public function sendMsg($deviceToken, $title, $message, $url = '')
	{
		//$registrationIds = array('fKpMxPhFoKI:APA91bFy0DacW1Kjnea63sqm5m8-tnJL_z6yxjDiabdGpSRjgc_vm2JhAcgX2qJc1hZ0VwtuptwOGWmyNkZWXKSC-gFvn0AtIsaQLGFnBt6sk_nO03KsXEQlmvFV2pju_Wi6QUFYiqgE');
		// prep the bundle
		$msg = array (
			'message' 	=> $message,
			'title'		=> $title,
			'url' => $url
			//'subtitle'	=> 'This is a subtitle. subtitle',
			//'tickerText'	=> 'Ticker text here...Ticker text here...Ticker text here',
			//'vibrate'	=> 1,
			//'sound'		=> 1,
			//'largeIcon'	=> 'large_icon',
			//'smallIcon'	=> 'small_icon'
		);
		$fields = array (
			'registration_ids' 	=> array($deviceToken),
			'data'			=> $msg
		);

		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $this->headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch );
		curl_close( $ch );
		return $result;
	}
}

