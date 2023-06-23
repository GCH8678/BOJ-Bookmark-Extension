import getApiUrl from "../../getApiUrl.js";

const ApiUrl = getApiUrl();
//const ApiUrl = "http://localhost:8080";
//const ApiUrl = "http://ec2-3-39-95-47.ap-northeast-2.compute.amazonaws.com:8080";
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
//     return;
// },{urls:["http://ec2-3-39-95-47.ap-northeast-2.compute.amazonaws.com:8080/api/bookmark/*"]},[]);
// callback: 이벤트가 발생할 때 호출되는 함수
// details : object요청에 대한 세부정보 => status(Integer)을 확인하기 위해 details.statusCode등으로 받아올 수 있음
// filter : 이 리스너로 보낼 이벤트를 제한하는 필터

chrome.runtime.onInstalled.addListener(()=>{
    chrome.storage.sync.set({isLoggedIn:false});
})


const login = async (email,password,sendResponse)=>{
    //console.log("getAuth method")
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
            //console.log("res in getAuth fetch")
            //console.log(res);
            //const data = 
            res.json().then((data)=>{
                console.log("res.json() in getAuth fetch");
                chrome.storage.sync.set({accessToken:data}, ()=>{
                    console.log("set data in getAuth fetch chrome storage api ");
                    console.log(data);
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
    // 이후 backend에도 logout 신호를 보내서 백엔드에서도 해당 Token의 값 지워줘야 함
    //chrome.storage.sync.remove('refreshToken')
    chrome.storage.sync.remove('accessToken');
        //console.log("logout method")
        chrome.storage.sync.set({isLoggedIn:false},(res)=>{
            sendResponse(false);
        });
}

const deleteBookmark= async (problemId,sendResponse) =>{
    //console.log("deleteBookmark Method 작동");
    //console.log("problemId : "+problemId);
    chrome.storage.sync.get("accessToken")
    .then((token)=>{
        const settings ={
            method: 'Delete',
            headers:{
                'Authorization': 'Bearer '+token.accessToken.accessToken,
                'Content-Type': 'application/json'
            },
        }
        fetch(
            ApiUrl+"/api/bookmark/"+problemId,
            settings
        ).then((res)=>{
            if(res.ok){
                //console.log("res.ok in delteBookmark fetch");
                sendResponse(true);
            }else{
                sendResponse(false);
                throw new Error("Invalid deleteBookmark request");
            }
        })
    })
}

const updateBookmark = async(problemId,afterday,sendResponse)=>{
    //console.log("updateBookmark Method 작동")
    //console.log("problemId : "+problemId);
    //console.log("afterday : "+afterday);
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        // Method : Patch 에서  Put으로 수정 -> fetch Api에서는 patch메소드 지원 안함
        const settings ={
            method: 'Put',
            headers:{
                'Authorization': 'Bearer '+token.accessToken.accessToken,
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
                //console.log("res.ok in updateBookmark fetch");
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

    //console.log("addBookmark Method 작동")
    //console.log("problemId : "+problemId);
    //console.log("afterday : "+afterday);
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        const settings ={
            method: 'Post',
            headers:{
                'Authorization': 'Bearer '+token.accessToken.accessToken,
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
                console.log("res.ok in addBookmark fetch");
                sendResponse(true);
            }else{
                sendResponse(false);
                throw new Error("Invalid addBookmark request");
            }
        })
    })
    .catch((err)=>{
        //console.log(err);
        sendResponse(false);
    })

}

const isBookmarked = (problemId,sendResponse)=>{
    //console.log("isBookmarked Method 작동")
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        const settings ={
            method: 'Get',
            headers:{
            'Authorization': 'Bearer '+token.accessToken.accessToken,
            'Content-Type': 'application/json'
            },
        }


        fetch(
                ApiUrl+"/api/bookmark/"+problemId,
                settings
        ).then((res)=>{
            if(res.ok){
                //console.log("res.ok in isBookmarked fetch");
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
    //console.log("getTodayProblemList Method 작동")
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        const settings ={
            method: 'Get',
            headers:{
            'Authorization': 'Bearer '+token.accessToken.accessToken,
            'Content-Type': 'application/json'
            },
        }


        fetch(
                ApiUrl+"/api/bookmark/list/today",
                settings
        ).then((res)=>{
            if(res.ok){
                //console.log("res.ok in getTodayProblemList fetch");
                res.json().then((res)=>{
                    sendResponse(res.problemNumList);
                    //console.log(res);
                    //console.log(res.problemNumList);
                })
            }else{
                throw new Error("Invalid getTodayProblemList request");
            }
        })
    })
    .catch((err)=>{
        //console.log(err);
        sendResponse(["invalid request : please login after logout or refresh popup page"]);
    })
}



chrome.runtime.onMessage.addListener((request,sender,sendResponse)=>{


    //로그인
    if (request.action === "login"){
        //console.log("login process In background");
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
        //console.log("addBookmark Listener");
        const {problemId,afterday} = request.data;
        addBookmark(problemId,afterday,sendResponse);
        return true;
    }
    //bookmark업데이트
    if(request.action === "updateBookmark"){
        //console.log("updateBookmark Listener");
        const {problemId,afterday} = request.data;
        updateBookmark(problemId,afterday,sendResponse);
        return true;
        
    }

    //bookmark 삭제
    if (request.action ==="deleteBookmark"){
        //console.log("deleteBookmark Listener");
        const {problemId} = request.data;
        deleteBookmark(problemId,sendResponse)
        return true;
    }

    //bookmark 여부 확인
    if (request.action === "isBookmarked"){
        //console.log("isBookmarked Listener")
        const {problemId} = request.data
        isBookmarked(problemId,sendResponse)
        return true;
    }

    //오늘 풀어야할 문제 정보 받기
    if (request.action === "getTodayProblemList"){
        //console.log("getTodayProblemList Listener")
        // 오늘 날짜 정보
        const today = new Date();
        getTodayProblemList(sendResponse);
        return true;
    }

    // accessToken check 및 갱신
    if (request.action === "checkAccessToken"){
        //console.log("accessToken check Listener")
        //
    }
})




// const getBookmarkList = async()=>{
//     const settings ={
//         method: 'Get',
//         headers:{
//             'Authorization': "Bearer "+chrome.storage.sync.get("access_token")
//         },
//         body: JSON.stringify({
//             notificationDate
//         }),
//     }
//     try{
//         const fetchResponse = await fetch(
//             ApiUrl+"/api/bookmark/list",
//             settings
//         )
//         const data = await fetchResponse.json()
//         return data;
//     }catch(err){
//         return err
//     }
// }