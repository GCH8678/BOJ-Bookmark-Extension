import getApiUrl from "../../getApiUrl.js";
import dayjs from 'dayjs';
import api from './AxiosLoader.js'

// const result = await api.get('/rooms)
// http://example.com/rooms

const ApiUrl = getApiUrl();

//chrome.webRequest.onBeforeRequest.addListener() -> redirect 또는 request를 취소할때 유용
//chrome.webRequest.onBeforeSendHeaders.addListener() -> Http 데이터 보내기 직전, headers는 사용가능 이후 상태 -> http request headers를 수정할때 유용
//  ㄴ> accessToken만료기간을 확인하고 수정 할 수 있음
// refreshToken 만료시 logout 
//    // 이후 backend에도 logout 신호를 보내서 백엔드에서도 해당 Token의 값 지워줘야 함
    //chrome.storage.sync.remove('refreshToken')
    //chrome.storage.sync.remove('accessToken');
    //     console.log("logout method")
    //     chrome.storage.sync.set({isLoggedIn:false},(res)=>{
    //         sendResponse(false);
    //     });
// chrome.webRequest.onResponseStarted.addListener((details)=>{
//     //console.log("onResponseStarted addListner 작동")
//     //console.log(details);
//     if(details.statusCode == 401){
//         // Token Refresh => /api/auth/reissue
//         // header에 accessToken, refreshToken 넣어야함
//     }
//     return;
// },{urls:["http://ec2-3-39-95-47.ap-northeast-2.compute.amazonaws.com:8080/api/bookmark/*"]},[]);

// callback: 이벤트가 발생할 때 호출되는 함수
// details : object요청에 대한 세부정보 => status(Integer)을 확인하기 위해 details.statusCode등으로 받아올 수 있음
// filter : 이 리스너로 보낼 이벤트를 제한하는 필터



chrome.runtime.onInstalled.addListener(()=>{
    chrome.storage.sync.set({isLoggedIn:false});
})


const validateAccessToken = async ()=>{
    chrome.storage.sync.get('jwtToken')
    .then((data)=>{
        if (dayjs.unix(data.jwtToken.accessTokenExpiration).diff(dayjs())>(60000*60)){ //1시간이상 남았을 때
            const settings = {
                method: "POST",
                header:{
                    'Authorization': `Bearer ${data.jwtToken.accessToken}`,
                    'Content-Type': 'application/json',
                }
            }
            fetch(`${ApiUrl}/api/auth/validate`,settings)
            .then((res)=>{
                if(res.ok){
                    return true
                }
                if(res.status==401){
                    refreshJwtToken()
                }
            })
        }else{
            logoutWithOutJwt()
        }
    })
}


const refreshJwtToken = async ()=>{
    chrome.storage.sync.get('jwtToken')
    .then((data)=>{
        if (dayjs(data.jwtToken.refreshTokenExpiration).diff(dayjs())>(60000*24*60)){ // 24시간 전에
            const settings = {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${data.jwtToken.accessToken}`,
                    'refresh-token': `${data.jwtToken.refreshToken}`,
                    'Content-Type': 'application/json'
                },
            }
            //console.log(dayjs(data.jwtToken.refreshTOkenExpiration).format('YYYY'))
            fetch(`${ApiUrl}/api/auth/reissue`,settings)
            .then((res)=>{
                if(res.ok){
                    res.json().then((data)=>{
                        chrome.storage.sync.set({jwtToken:data}, ()=>{
                            //console.log(data);
                            sendResponse(true);
                    })})
                }else if(res.status === 401){
                    logoutWithOutJwt()
                }
            })
        }else{
            logoutWithOutJwt();
        }
    })
    
}


const login = async (email,password,sendResponse)=>{
    const settings = {
        method: 'POST',
        body: JSON.stringify({
            email,
            password,
        }),
        headers: { 'Content-Type': 'application/json' }
    }
    fetch(
            ApiUrl+"/api/auth/login",
            settings
    ).then((res)=>{
        if(res.ok){
            res.json().then((data)=>{
                //console.log("res.json() in getAuth fetch");
                //console.log(data)
                chrome.storage.sync.set({jwtToken:data}, ()=>{
                    //console.log(data);
                    sendResponse(true);
            })
            });
        }else{
            throw new Error("Invalid login attempt");
        }
    })
    .catch((error)=>{
        //console.log(error <-"<- error in getAuth")
        sendResponse(false)
    })
}

const logout = async (sendResponse) =>{
    chrome.storage.sync.get('jwtToken')
    .then((data)=>{
        const settings = {
            method: 'POST',
            headers:{
                'Authorization': `Bearer ${data.jwtToken.accessToken}`,
                'Content-Type': 'application/json'
            },            
        }
        fetch(
            `${ApiUrl}/api/auth/logout`,
            settings
        )
    })
    .then(()=>{    
        chrome.storage.sync.remove('jwtToken');
    })
    .then(()=>{
        chrome.storage.sync.set({isLoggedIn:false},(res)=>{
            sendResponse(false);
        });
    })   
}

const logoutWithOutJwt = async()=>{
    chrome.storage.sync.remove('jwtToken');
    chrome.storage.sync.set({isLoggedIn:false});
}


const deleteBookmark= async (problemId,sendResponse) =>{
    chrome.storage.sync.get("jwtToken")
    .then((data)=>{
        const settings ={
            method: 'Delete',
            headers:{
                'Authorization': 'Bearer '+data.jwtToken.accessToken,
                'Content-Type': 'application/json'
            },
        }
        fetch(
            ApiUrl+"/api/bookmark/"+problemId,
            settings
        ).then((res)=>{
            if(res.ok){
                sendResponse(true);
            }else{
                sendResponse(false);
                throw new Error("Invalid deleteBookmark request");
            }
        })
    })
}

const updateBookmark = async(problemId,afterday,sendResponse)=>{
    chrome.storage.sync.get("jwtToken")
    .then((data)=>{

        // Method : Patch 에서  Put으로 수정 -> fetch Api에서는 patch메소드 지원 안함
        const settings ={
            method: 'Put',
            headers:{
                'Authorization': 'Bearer '+data.jwtToken.accessToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                problemId,
                afterday
            }),
        }


        fetch(
                ApiUrl+"/api/bookmark",
                settings
        ).then((res)=>{
            if(res.ok){
                sendResponse(true);
            }else{
                sendResponse(false);
                throw new Error("Invalid updateBookmark request");
            }
        })
    })
    .catch((err)=>{
        //console.log(err);
        sendResponse(false);
    })

}

const addBookmark = async(problemId,afterday,sendResponse)=>{

    chrome.storage.sync.get("jwtToken")
    .then((data)=>{

        const settings ={
            method: 'Post',
            headers:{
                'Authorization': 'Bearer '+data.jwtToken.accessToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                problemId,
                afterday
            }),
        }


        fetch(
                ApiUrl+"/api/bookmark",
                settings
        ).then((res)=>{
            if(res.ok){
                //console.log("res.ok in addBookmark fetch");
                sendResponse(true);
            }else{
                sendResponse(false);
                throw new Error("Invalid addBookmark request");
            }
        })
    })
    .catch((err)=>{
        sendResponse(false);
    })

}

const isBookmarked = (problemId,sendResponse)=>{
    chrome.storage.sync.get("jwtToken")
    .then((data)=>{

        const settings ={
            method: 'Get',
            headers:{
            'Authorization': 'Bearer '+data.jwtToken.accessToken,
            'Content-Type': 'application/json'
            },
        }


        fetch(
                ApiUrl+"/api/bookmark/"+problemId,
                settings
        ).then((res)=>{
            if(res.ok){
                res.json().then((data)=>{
                    sendResponse(data);
                })
            }else{
                throw new Error("not res.ok in isBookmarked fetch");
            }
        })
    })
    .catch((err)=>{
        //console.log(err);
        sendResponse(false);
    })
}

const getTodayProblemList = (sendResponse) =>{
    chrome.storage.sync.get("jwtToken")
    .then((data)=>{

        const settings ={
            method: 'Get',
            headers:{
            'Authorization': 'Bearer '+data.jwtToken.accessToken,
            'Content-Type': 'application/json'
            },
        }


        fetch(
                ApiUrl+"/api/bookmark/list/today",
                settings
        ).then((res)=>{
            if(res.ok){
                res.json().then((res)=>{
                    sendResponse(res.problemNumList);
                })
            }else{
                throw new Error("Invalid getTodayProblemList request");
            }
        })
    })
    .catch((err)=>{
        sendResponse(["invalid request : please login after logout or refresh popup page"]);
    })
}



chrome.runtime.onMessage.addListener((request,sender,sendResponse)=>{

    
    //로그인
    if (request.action === "login"){
        const {email,password} = request.data;
        login(email,password,sendResponse) //getAuth 비동기
        return true;
    }
    
    //로그아웃
    if (request.action === "logout"){
        logout(sendResponse);
        return true
    }


    //bookmark추가
    if (request.action === "addBookmark"){
        const {problemId,afterday} = request.data;
        addBookmark(problemId,afterday,sendResponse);
        return true;
    }
    //bookmark업데이트
    if(request.action === "updateBookmark"){
        const {problemId,afterday} = request.data;
        updateBookmark(problemId,afterday,sendResponse);
        return true;
        
    }

    //bookmark 삭제
    if (request.action ==="deleteBookmark"){
        const {problemId} = request.data;
        deleteBookmark(problemId,sendResponse)
        return true;
    }

    //bookmark 여부 확인
    if (request.action === "isBookmarked"){
        const {problemId} = request.data
        isBookmarked(problemId,sendResponse)
        return true;
    }

    //오늘 풀어야할 문제 정보 받기
    if (request.action === "getTodayProblemList"){
        // 오늘 날짜 정보
        const today = new Date();
        getTodayProblemList(sendResponse);
        return true;
    }

})


