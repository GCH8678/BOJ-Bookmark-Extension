

chrome.runtime.onInstalled.addListener(()=>{
    chrome.storage.sync.set({isLoggedIn:false});
})

const login = async (email,password,sendResponse)=>{
    console.log("getAuth method")
    const settings = {
        method: 'POST',
        body: JSON.stringify({
            email,
            password,
        }),
        headers: { 'Content-Type': 'application/json' }
    }
    fetch(
            "http://localhost:8080/api/auth/login",
            settings
    ).then((res)=>{
        if(res.ok){
            console.log("res in getAuth fetch")
            console.log(res);
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
        console.log(error <-"<- error in getAuth")
        sendResponse(false)
    })
}

const logout = async (sendResponse) =>{
    chrome.storage.sync.remove('accessToken');
        console.log("logout method")
        chrome.storage.sync.set({isLoggedIn:false},(res)=>{
            sendResponse(false);
        });
}


const addBookmark = async(problemId,sendResponse)=>{

    console.log("addBookmark Method 작동")
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        const settings ={
            method: 'Post',
            headers:{
            'Authorization': 'Bearer '+token.accessToken.accessToken,
            'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                problemId
            }),
        }


        fetch(
                "http://localhost:8080/api/bookmark",
                settings
        ).then((res)=>{
            if(res.ok){
                console.log("res.ok in addBookmark fetch");
                sendResponse(true);
            }else{
                throw new Error("Invalid addBookmark request");
            }
        })
        // .catch((error)=>{
        //     console.log(error <-"<- error in getAuth");
        //     sendResponse(false);
        // })
    })
    .catch((err)=>{
        console.log(err);
        sendResponse(false);
    })

}

const isBookmarked = (problemId,sendResponse)=>{
    console.log("isBookmarked Method 작동")
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
                "http://localhost:8080/api/bookmark/"+problemId,
                settings
        ).then((res)=>{
            if(res.ok){
                console.log("res.ok in isBookmarked fetch");
                res.json().then((res)=>sendResponse(res.isBookmarked))
            }else{
                throw new Error("Invalid isBookmarked request");
            }
        })
    })
    .catch((err)=>{
        console.log(err);
        sendResponse(false);
    })
}

const getTodayProblemList = (today,sendResponse) =>{
    console.log("getTodayProblemList Method 작동")
    chrome.storage.sync.get("accessToken")
    .then((token)=>{

        const settings ={
            method: 'Get',
            headers:{
            'Authorization': 'Bearer '+token.accessToken.accessToken,
            'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                today
            }),
        }


        fetch(
                "http://localhost:8080/api/notification/",
                settings
        ).then((res)=>{
            if(res.ok){
                console.log("res.ok in getTodayProblemList fetch");
                res.json().then((res)=>sendResponse(res.isBookmarked))
            }else{
                throw new Error("Invalid getTodayProblemList request");
            }
        })
    })
    .catch((err)=>{
        console.log(err);
        sendResponse(false);
    })
}



chrome.runtime.onMessage.addListener((request,sender,sendResponse)=>{
    //로그인
    if (request.action === "login"){
        console.log("login process In background");
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
        console.log("Bookmark Listener")
        const {problemId} = request.data
        addBookmark(problemId,sendResponse)
        return true
    }

    //bookmark 여부 확인
    if (request.action === "isBookmarked"){
        console.log("isBookmarked Listener")
        const {problemId} = request.data
        isBookmarked(problemId,sendResponse)
        return true;
    }

    //오늘 풀어야할 문제 정보 받기
    if (request.action === "getTodayProblemList"){
        console.log("getTodayProblemList Listener")
        // 오늘 날짜 정보
        const today = new Date();
        getTodayProblemList(today,sendResponse);
        return true;
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
//             "http://localhost:8080/api/bookmark/list",
//             settings
//         )
//         const data = await fetchResponse.json()
//         return data;
//     }catch(err){
//         return err
//     }
// }