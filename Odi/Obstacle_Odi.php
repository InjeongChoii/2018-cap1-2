<?php
    $db_user ="root";    //추가한 계정이름(사용자명)
    $db_pass ="";     //비밀번호
    $db_host ="localhost";  
    $db_name ="probono";
    
    $con = mysqli_connect($db_host, $db_user, $db_pass, $db_name);
    
    $locationX = $_POST["locationX"];
    $locationY = $_POST["locationY"];
    $type = $_POST["type"];
    $userID = $_POST["userID"];
    
    //$result = mysqli_query($con, "select locationX,locationY from obstacle where locationX between ? and ? and locationY between ? and ?;");
    //$response = array();
    
 
    $statement = mysqli_prepare($con, "select locationX,locationY from obstacle where locationX between ? and ? and locationY between ? and ?");
    mysqli_stmt_bind_param($statement, "ssss", $locationX, $locationY);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $locationX, $locationY);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;
        $response["locationX"] = $locationX;
        $response["locationY"] = $locationY;
    }
    echo json_encode($response);
    
    while($row = mysqli_fetch_array($result)){
        array_push($response, array('locationX'=>$row[0],'locationY'=>$row[1]));
    }
    echo json_encode(array("response"=>$response));
    mysqli_close($con);
    ?>