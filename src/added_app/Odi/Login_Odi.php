<?php
    $db_user ="root";    //추가한 계정이름(사용자명)
    $db_pass ="";     //비밀번호
    $db_host ="localhost";  
    $db_name ="probono";
    
    $con = mysqli_connect($db_host, $db_user, $db_pass, $db_name);

$userID = $_POST["userID"];
$userName = $_POST["userName"];
$dORv = $_POST["dORv"];
$carer = $_POST["carer"];

$statement = mysqli_prepare($con, "select * from user where userID = ?");
mysqli_stmt_bind_param($statement, "s", $userID);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userID, $userName, $dORv, $carer);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)){
    $response["success"] = true;
    $response["userID"] = $userID;
    $response["userName"] = $userName;
    $response["dORv"] = $dORv;
    $response["carer"] = $carer;
}
echo json_encode($response);
?>