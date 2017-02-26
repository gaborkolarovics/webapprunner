<?php

define('__DS__', DIRECTORY_SEPARATOR);
define('__WWWDIR__', dirname(__FILE__) . __DS__);
define('__LOGDIR__', __WWWDIR__ . 'logs' . __DS__);
define('__LIBS__', __WWWDIR__ . 'libs' . __DS__);
define('__TABLES__', __WWWDIR__ . 'tables' . __DS__);

require_once(__LIBS__ . 'Log.class.php');

$Log = new Log();