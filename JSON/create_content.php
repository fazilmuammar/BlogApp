<?php
	
	require_once('connection.php');

	$category_id	= $_REQUEST['category_id'];
	$user_id		= $_REQUEST['user_id'];
	$username		= $_REQUEST['username'];   
	$author			= $_REQUEST['author'];
	$title			= $_REQUEST['title'];
	$content		= $_REQUEST['content'];
	$latitude		= $_REQUEST['latitude'];
	$longitude		= $_REQUEST['longitude'];
	$image			= $_REQUEST['image'];
	$date			= date('Y-m-d H:i:s');
	

    $filename   = date('dmY');
    $rand       = uniqid();
	$id 		= $filename.$rand;
	$root 		= $_SERVER['DOCUMENT_ROOT'];
	$path 		= $root."/BlogApp/images/$id.png";
	$actualpath = "$id.png";
	
	// insert content
	$query		= mysqli_query($conn, "INSERT INTO content (latitude, longitude, category_id, user_id, title, author, content, image, created, updated, status, view, love) VALUES ('$latitude', '$longitude','$category_id', '$user_id', '$title', '$author', '$content', '$actualpath', '$date', '$date', 'confirm', '0', '0') ");

	if($query) {
	    file_put_contents($path,base64_decode($image));
	    echo json_encode(array( 'response'=>'success' ));
	} else {
		echo json_encode(array( 'response'=>'failed' ));
	}
?>