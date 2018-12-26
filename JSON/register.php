<?php
	
	require_once('connection.php');

	$username	= $_REQUEST['username'];
	$email		= $_REQUEST['email'];
	$phone		= $_REQUEST['phone'];    
	$password	= md5($_REQUEST['password']);
	$date		= date('Y-m-d H:i:s');
	
	// insert user
	$query1 = mysqli_query($conn, "INSERT INTO User (username, email, phone, password, created, updated, status) VALUES ('$username', '$email', '$phone', '$password', '$date', '$date', 'confirm') ");
	
	if($query1) {
	    echo json_encode(array( 'response'=>'success' ));
	} else {
		echo json_encode(array( 'response'=>'failed' ));
	}
?>