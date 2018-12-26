<?php
	
	require_once('connection.php');

	$email		= $_REQUEST['email'];   
	$password	= md5($_REQUEST['password']); 
	$log		= 0;
	$date		= date('Y-m-d H:i:s');
	
	// select user
	$query1 = mysqli_query($conn, "SELECT * FROM User WHERE (username='$email' OR email='$email') AND password='$password' AND status='confirm'");
	while ($row = mysqli_fetch_row($query1)) { $log ++; }

	if($log > 0) {

		// get data user
	    $query2 = mysqli_query($conn, "SELECT * FROM User WHERE (username='$email' OR email='$email') AND password='$password' ");

		while ($row = mysqli_fetch_assoc($query2)) { 
			
			$user_id 	= $row['user_id'];
			$username	= $row['username'];
			$email 		= $row['email'];
			$phone 		= $row['phone'];
			$created 	= $row['created'];
			$updated 	= $row['updated'];
			$picture 	= $row['picture'];

		}

	    echo json_encode(array(
	    	'response'		=>'success',
	    	'user_id'		=> $user_id,
	    	'username'		=> $username,
	    	'email'			=> $email,
	    	'phone'			=> $phone,
	    	'picture'		=> $picture,
	    	'created'		=> $created,
	    	'updated'		=> $updated
	    	));

	} else {
	    echo json_encode(array( 'response'=>'failed' ));
	}
?>