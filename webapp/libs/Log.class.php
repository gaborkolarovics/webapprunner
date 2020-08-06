<?php 
       /* *
	* Log 			A logger class which creates logs when an exception is thrown.
	* @author		Author: Vivek Wicky Aswal. (https://twitter.com/#!/VivekWickyAswal)
	* @git 			https://github.com/indieteq/PHP-MySQL-PDO-Database-Class
	* @version      0.1a
	*/
	class Log {
			
		    # @string, Log directory name
		    	private $path = '';
			
		    # @void, Default Constructor, Sets the timezone and path of the log files.
			public function __construct() {
				date_default_timezone_set('Europe/Amsterdam');	
				$this->path  = __LOGDIR__;
			}
			
		   /**
		    *   @void 
		    *	Creates the log
		    *
		    *   @param string $message the message which is written into the log.
		    *	@description:
		    *	 1. Checks if directory exists, if not, create one and call this method again.
	            *	 2. Checks if log already exists.
		    *	 3. If not, new log gets created. Log is written into the logs folder.
		    *	 4. Logname is current date(Year - Month - Day).
		    *	 5. If log exists, edit method called.
		    *	 6. Edit method modifies the current log.
		    */	
			public function write($message) {
				$date = new DateTime();
				$log = $this->path . $date->format('Y-m').".txt";

				if(is_dir($this->path)) {
						$fh  = fopen($log, 'a+') or die("Fatal Error !");
						$logcontent = $date->format('Y.m.d H:i:s')." : " . $message ."\r\n";
						fwrite($fh, $logcontent);
						fclose($fh);
				}
				else {
					  if(mkdir($this->path,0777) === true) 
					  {
 						 $this->write($message);  
					  }
				}
			}
		}
