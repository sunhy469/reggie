function loginApi(data){
    return $axios({
        url: '/user/login',
        method: 'post',
        data: data
    })
}
function sendMsgApi(data){
    return $axios({
        url: '/user/sendMsg',
        method: 'post',
        data: data
    })
}

function loginoutApi(){
    return $axios({
        url: '/user/loginout',
        method: 'post'
    })
}