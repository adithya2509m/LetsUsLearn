<?php
require_once('connection.php');


if(mysql_select_db( 'letslearn', $connection )){
#echo "lol";
}


if(isset($_GET['id']))
{

	$id = $_GET['id'];
	
		
	$reg_array = array();
	
	$sql="SELECT * 
FROM  `user` ,  `teach` ,  `learn` 
WHERE learn.id =$id
AND teach.topic = learn.topic
AND teach.id = user.id";
	$result=mysql_query( $sql, $connection );
	
	
	
$rows = array();
while($r = mysql_fetch_assoc($result)) {
	#echo $r;    
$rows[] = $r;
}

#echo json_encode($rows);

	$reg_array['success']=1;
	$reg_array['learn']=json_encode($rows);
	$sql="SELECT * 
FROM  `user` ,  `teach` ,  `learn` 
WHERE teach.id =$id
AND teach.topic = learn.topic
AND teach.id = user.id";
	$result=mysql_query( $sql, $connection );
	
	
	
$rows = array();
while($r = mysql_fetch_assoc($result)) {
#echo $r;
}
	$reg_array['teach']=json_encode($rows);
	echo json_encode($reg_array);
			
		
	
	

	}
else
{
	echo "not set";
}
?>
