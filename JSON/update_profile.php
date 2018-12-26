<?php
	require_once('connection.php');

	$user_id	= $_REQUEST['user_id'];
	$username	= $_REQUEST['username'];
	$email		= $_REQUEST['email'];
	$phone		= $_REQUEST['phone'];
	$password	= $_REQUEST['password'];
	$date		= date('Y-m-d H:i:s');

	// update user
	$query = mysqli_query($conn, "UPDATE user SET email='$email', phone='$phone',password='$password', updated='$date' WHERE user_id='$user_id'");
	if($query) {
	    echo json_encode(array( 'response'=>'success' ));
	} else {
	    echo json_encode(array( 'response'=>'failed' ));
	}
?>