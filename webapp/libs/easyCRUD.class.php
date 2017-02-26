<?php 
/**
* Easy Crud  -  This class kinda works like ORM. Just created for fun :) 
*
* @author		Author: Vivek Wicky Aswal. (https://twitter.com/#!/VivekWickyAswal)
* @version      0.1a
*/
require_once(__LIBS__ . 'Db.class.php');
class Crud {

	private $db;

	public $variables;

	public function __construct($data = array(), &$db = null) {
		if($db === null) {
			$this->db =  new DB();
		} else {
			$this->db = $db;
		}
		foreach($data as $k => $v){
			 $this->__set($k,$v);
		}
	}

	public function __set($name,$value){
		if(strtolower($name) === $this->pk) {
			$this->variables[$this->pk] = $value;
		}
		else {
			$this->variables[$name] = $value;
		}
	}

	public function __get($name)
	{	
		if(is_array($this->variables)) {
			if(array_key_exists($name,$this->variables)) {
				return $this->variables[$name];
			}
		}

		$trace = debug_backtrace();
		trigger_error(
		'Undefined property via __get(): ' . $name .
		' in ' . $trace[0]['file'] .
		' on line ' . $trace[0]['line'],
		E_USER_NOTICE);
		return null;
	}

	public function save($id = "0") {
		$this->variables[$this->pk] = (empty($this->variables[$this->pk])) ? $id : $this->variables[$this->pk];

		$fieldsvals = '';
		$columns = array_keys($this->variables);

		foreach($columns as $column)
		{
			if($column !== $this->pk)
			$fieldsvals .= $column . " = :". $column . ",";
		}

		$fieldsvals = substr_replace($fieldsvals , '', -1);

		if(count($columns) > 1 ) {
			$sql = "UPDATE " . $this->table .  " SET " . $fieldsvals . " WHERE " . $this->pk . "= :" . $this->pk;
			return $this->db->query($sql,$this->variables);
		}
	}

	public function create() { 
		$bindings   	= $this->variables;

		if(!empty($bindings)) {
			$fields     =  array_keys($bindings);
			$fieldsvals =  array(implode(",",$fields),":" . implode(",:",$fields));
			$sql 		= "INSERT INTO ".$this->table." (".$fieldsvals[0].") VALUES (".$fieldsvals[1].")";
		}
		else {
			$sql 		= "INSERT INTO ".$this->table." () VALUES ()";
		}

		return $this->db->query($sql,$bindings);
	}

	public function delete($id = "") {
		$id = (empty($this->variables[$this->pk])) ? $id : $this->variables[$this->pk];

		if(!empty($id)) {
			$sql = "DELETE FROM " . $this->table . " WHERE " . $this->pk . "= :" . $this->pk. " LIMIT 1" ;
			return $this->db->query($sql,array($this->pk=>$id));
		}
	}

	public function find($_fields = array()) {
		$columns = array_keys($_fields);
		$fieldsvals = ' ';
		foreach($columns as $column)
		{
			$fieldsvals .= $column . " = :".$column." AND ";
		}
		$fieldsvals = substr_replace($fieldsvals , '', -4);
		if(count($columns) > 0 ) {
			$sql = "SELECT * FROM " . $this->table ." WHERE " . $fieldsvals . " LIMIT 0,1";
			return $this->db->row($sql,$_fields);
		}	
		return array();
		
		
		
		//if(!empty($id)) {
		//	$sql = "SELECT * FROM " . $this->table ." WHERE " . $this->pk . "= :" . $this->pk . " LIMIT 1";	
   //  $this->variables = $this->db->row($sql,array($this->pk=>$id));
   //  return is_array($this->variables);
		//} else {
		//  return false;
		//}
	}
	
	public function search($_fields = array(), $_like = false, $from = 0, $count = 25, $orderBy = array(), $groupBy = array()) {
		$columns = array_keys($_fields);
		$fieldsvals = ' ';
		foreach($columns as $column)
		{
			if($_like) {
				$fieldsvals .= $column . " LIKE  CONCAT(:".$column.", '%') AND ";
			} else {
				$fieldsvals .= $column . " = :".$column." AND ";
			}
		}
		$fieldsvals = substr_replace($fieldsvals , '', -4);
		$fieldsvals .= (0 === count($groupBy) ) ? ' ' : ' GROUP BY ';		
		foreach($groupBy as $column => $order)
		{
			$fieldsvals .= " ". $column. "  ". $order . ",";
		}
		if(count($groupBy) > 0) {
			$fieldsvals = substr_replace($fieldsvals , '', -1);
		}
		$fieldsvals .= (0 === count($orderBy) ) ? ' ' : ' ORDER BY ';		
		foreach($orderBy as $column => $order)
		{
			$fieldsvals .= " ". $column . "  ". $order . ",";
		}		
   if(count($orderBy) > 0) {
			$fieldsvals = substr_replace($fieldsvals , '', -1);
		}
		if(count($columns) > 0 ) {
			$sql = "SELECT * FROM " . $this->table ." WHERE " . $fieldsvals . " LIMIT ".intval($from).",".intval($count);
			return $this->db->query($sql,array_merge($_fields, $orderBy, $groupBy));
		}	
		return array();
	}

	public function all(){
		return $this->db->query("SELECT * FROM " . $this->table);
	}
	
	public function min($field)  {
		if($field)
		return $this->db->single("SELECT min(" . $field . ")" . " FROM " . $this->table);
	}

	public function max($field)  {
		if($field)
		return $this->db->single("SELECT max(" . $field . ")" . " FROM " . $this->table);
	}

	public function avg($field)  {
		if($field)
		return $this->db->single("SELECT avg(" . $field . ")" . " FROM " . $this->table);
	}

	public function sum($field)  {
		if($field)
		return $this->db->single("SELECT sum(" . $field . ")" . " FROM " . $this->table);
	}

	public function count($field)  {
		if($field)
		return $this->db->single("SELECT count(" . $field . ")" . " FROM " . $this->table);
	}	
	
}
?>
