import axios from "axios";
import { getAuthToken } from "./helper";
import type { UserRequest } from "./types/User";
import type { RegistrationUserRequest } from "./types/Register";
import type { LoginUserRequest } from "./types/Login";
import type { BookingRequest } from "./types/Booking";
import type { TrainingSessionRequest } from "./types/TrainingSession";

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

//user methods
export const getUsers = () => apiGetCall("user/get")
export const getUserById = (userId: string) => apiGetCall(`user/getById?id=${userId}`)
export const registerUser = (req: RegistrationUserRequest) => apiPostCall<RegistrationUserRequest>("user/register", req)
export const loginUser = (req: LoginUserRequest) => apiPostCall<LoginUserRequest>("user/login", req)
export const updateUser = (req: UserRequest) => apiPutCall<UserRequest>("/user/put", req)
export const deleteUser = (userId: string) => apiDeleteCall(`user/delete?id=${userId}`)

//booking methods
export const getBookings = () => apiGetCall("booking/get")
export const getBookingById = (bookingId: string) => apiGetCall(`booking/getById?id=${bookingId}`)
export const createBooking = (req: BookingRequest) => apiPostCall<BookingRequest>("booking/post", req)
export const updateBooking = (req: BookingRequest) => apiPutCall<BookingRequest>("booking/put", req)
export const deleteBooking = (bookingId: string) => apiDeleteCall(`booking/delete?id=${bookingId}`)

//training session methods
export const getTrainingSessions = () => apiGetCall("trainingSessions/get")
export const getTrainingSessionById = (trainingSessionId: string) => apiGetCall(`trainingSession/getById?id=${trainingSessionId}`)
export const createTrainingSession = (req: TrainingSessionRequest) => apiPostCall<TrainingSessionRequest>("trainingSession/post", req)
export const updateTrainingSession = (req: TrainingSessionRequest) => apiPutCall<TrainingSessionRequest>("trainingSession/put", req)
export const deleteTrainingSession = (trainingsessionId: string) => apiDeleteCall(`trainingSession/delete?id=${trainingsessionId}`) 