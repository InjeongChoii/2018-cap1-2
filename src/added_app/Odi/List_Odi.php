<?php
    $db_user ="root";    //추가한 계정이름(사용자명)
    $db_pass ="";     //비밀번호
    $db_host ="localhost";  
    $db_name ="probono";
    
    $con = mysqli_connect($db_host, $db_user, $db_pass, $db_name);
    $result = mysqli_query($con, "select * from user;");
    $response = array();
    
    
    while($row = mysqli_fetch_array($result)){
        array_push($response, array('userID'=>$row[0],'userName'=>$row[1],'dORv'=>$row[2],'carer'=>$row[3]));
    }
    echo json_encode(array("response"=>$response));
    mysqli_close($con);
    ?>
