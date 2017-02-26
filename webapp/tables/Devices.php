<?php 
	require_once(__LIBS__ . "easyCRUD.class.php");

	class Devices Extends Crud {
		
			# Your Table name 
			protected $table = 'devices';
			
			# Primary Key of the Table
			protected $pk	 = '_id';
	}

?>