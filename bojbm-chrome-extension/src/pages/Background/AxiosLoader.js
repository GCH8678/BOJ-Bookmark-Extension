import axios from 'axios';
import getApiUrl from "../../getApiUrl"
import fetchAdapter from '@vespaiach/axios-fetch-adapter';


const ApiUrl = getApiUrl();

const api = axios.create({
    baseURL: ApiUrl,
    headers:{"Content-type":"application/json"}, // data type
    adapter: fetchAdapter
})

api.interceptors.request.use(
    async function(config){
        const token = (await chrome.storage.sync.get("jwtToken")).jwtToken;

        //요청시 AccessToken 계속 보내주기
        if(!token){
            config.headers.accessToken = null;
            config.headers.refreshToken = null;
        }
        if (config.headers && token){
            const{accessToken,refreshToken}  = Json.parse(token);
            config.headers.Authorization = `Bearer ${accessToken}`;
            config.headers.refreshToken = `${refreshToken}`;
            return config
        }
        console.log("request start",config);
    },
    function(error){
        console.log("request error",error);
        return Promise.reject(error);
    }
)

api.interceptors.response.use(
    function (response){
        console.log("get response",response);
        return response;
    },
    async (error) =>{
        const{
            config,
            response: {status},
        } = error;
        if(status ===401){
            if (error.response.data.message === "expired"){
                const originalRequest = config;
                const jwtToken = (await chrome.storage.sync.get("jwtToken")).jwtToken

                // token Refresh 요청
                const {data}  = await axios.post(
                    `${ApiUrl}/reissue`,
                    {},
                    {headers:{"Authorization" : `Bearer ${jwtToken.refreshToken}`,
                            "refresh-token" : `${jwtToken.refreshToken}`
                        }
                    }
                )
                await chrome.storage.sync.set({jwtToken:data})
                originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
                return axios(originalRequest);
            }
        }
        console.log("response error",error);
        return Promise.reject(error)
    }
);

export default api;