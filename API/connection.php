<?php

	$conn = mysql_connect("matheusnavega.com.br", "matheusn_admin", "reclameonibus06");
	mysql_select_db("matheusn_reclameonibus",$conn);
	mysql_set_charset("UTF8");

	header ('Content-type: application/json; charset=utf-8');

?>