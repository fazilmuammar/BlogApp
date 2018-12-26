<?php
	require_once('connection.php');

	$user_id	= $_REQUEST['user_id'];
	$picture	= $_REQUEST['picture'];
	$date		= date('Y-m-d H:i:s');


    $filename	= date('dmY');
    $rand       = uniqid();
	$id 		= $filename.$rand;
	$root 		= $_SERVER['DOCUMENT_ROOT'];
	$path 		= $root."/BlogApp/pictures/$id.png";
	$actualpath	= "$id.png";
	
	// update
	$sql = "UPDATE user SET picture='$actualpath', updated='$date' WHERE user_id='$user_id'";
	$query1 = mysqli_query($conn, $sql);

	if($query1) {
	    file_put_contents($path, base64_decode($picture));
	    echo json_encode(array( 'response'=>'success', 'picture'=>$actualpath ));
	} else {
		echo json_encode(array( 'response'=>'failed' ));
	}
?>