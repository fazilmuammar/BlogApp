<?php
	require_once('connection.php');

	$content_id	= $_REQUEST['content_id'];
	$user_id	= $_REQUEST['user_id'];
	$date		= date('Y-m-d H:i:s');
	$love		= 0;

	// select love
	$query = mysqli_query($conn, "SELECT * FROM love WHERE user_id='$user_id' AND content_id='$content_id'");
	while ($row = mysqli_fetch_row($query)) { $love ++; }

	if ($love > 0) {
		echo json_encode(array( 'response'=>'success' ));
	} else {

		$query1 = mysqli_query($conn, "UPDATE content SET love=love+1 WHERE content_id='$content_id'");
		$query2 = mysqli_query($conn, "INSERT INTO love (content_id, user_id, datetime) VALUES ('$content_id', '$user_id', '$date')");
		
		if($query1 && $query2) {
		    echo json_encode(array( 'response'=>'success' ));
		} else {
		    echo json_encode(array( 'response'=>'failed' ));
		}
		
	}
?>