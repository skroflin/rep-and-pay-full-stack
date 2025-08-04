import axios from "axios";
import { getAuthToken } from "./helper";

const BASE_URL = 'http://localhost:8080'

async function apiGetCall(
    route: string
){
    const res = await axios.get(
        `${BASE_URL}/api/fina/skroflin/${route}`,
        {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": `Bearer ${getAuthToken}`
            }
        }
    )
    return res.data
}

async function apiPostCall<Req>(
    route: string, 
    data: Req
) {
    const res = await axios.post(
        `${BASE_URL}/api/fina/skroflin/${route}`,
        data,
        {
           headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": `Bearer ${getAuthToken}`
            } 
        }
    )    
    return res.data
}

async function apiPutCall<Req>(
    route: string,
    data: Req
) {
   const res = await axios.put(
        `${BASE_URL}/api/fina/skroflin/${route}`,
        data,
        {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": `Bearer ${getAuthToken}`
            }
        }
   )
   return res.data 
}

async function apiDeleteCall(
    route: string
) {
    const res = await axios.delete(
        `${BASE_URL}/api/fina/skroflin/${route}`,
        {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": `Bearer ${getAuthToken}`
            }
        }
    )
    return res.data
}