<?php
	require_once('connection.php');

	$user_id	= $_REQUEST['user_id'];
	$username	= $_REQUEST['username'];
	$content_id	= $_REQUEST['content_id'];
	$content	= $_REQUEST['content'];
	$date		= date('Y-m-d H:i:s');
	
	// insert comment
	$query = mysqli_query($conn, "INSERT INTO comments (user_id, content_id, datetime, content) VALUES ('$user_id', '$content_id', '$date', '$content') ");
	
	$response	= array();
	if($query) {
		echo json_encode(array( 'response'=>'success' ));
	} else {
		echo json_encode(array( 'response'=>'failed' ));
	}
?>