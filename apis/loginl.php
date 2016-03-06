<?php
require_once('connection.php');


if(mysql_select_db( 'letslearn', $connection )){
#echo "lol";
}


if(isset($_POST['uname']) && isset($_POST['password']) && isset($_POST['long']) && isset($_POST['lat']))
{

	$id = $_POST['uname'];
	$lat=isset($_POST['long']);
	$long=isset($_POST['lat']);
	
	$password=$_POST['password'];
#	$password=password_hash($password, PASSWORD_DEFAULT);
		
	$reg_array = array();
	
	$sql="SELECT * from user where uname='$id' and password='$password'";
	$result=mysql_query( $sql, $connection );
	
	if(mysql_num_rows($result)!= 0){
	
	$reg_array['success']=1;
	$reg_array['message']="Login Successful";
	$sql="SELECT * from user where uname='$id'";
	$result=mysql_query( $sql, $connection );
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	$reg_array['id']=$row['id'];
	
	$sql="update user set lat='$lat',long='$long' where uname='$id'";
	$result=mysql_query( $sql, $connection );
	
	echo json_encode($reg_array);
			
		
	
	}else{
	$reg_array['success']=0;
	$reg_array['message']="Incorrect username or password or user does not exist";
echo json_encode($reg_array);
}

	}
else
{
	echo "not set";
}
?>
