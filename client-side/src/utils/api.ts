import axios from "axios";
import { getAuthToken, setAuthToken, setRole, setUsername } from "./helper";
import type { UserRequest } from "./types/user/User";
import type { RegistrationUserRequest } from "./types/user/Register";
import type { LoginUserRequest } from "./types/user/Login";
import type { BookingRequest, UpdateBookingStatusRequest } from "./types/Booking";
import type { TrainingSessionRequest } from "./types/TrainingSession";
import type { PasswordUserRequest } from "./types/user/Password";
import type { MyTrainingSessionRequest } from "./types/user-authenticated/MyTrainingSession";
import type { MyBookingRequest } from "./types/user-authenticated/MyBooking";
import type { CheckoutRequest } from "./types/Checkout";
import type { JwtResponse } from "./types/user/JwtResponse";

const BASE_URL = 'http://localhost:8080'

async function apiGetCall(
    route: string
) {
    const res = await axios.get(
        `${BASE_URL}/api/fina/skroflin/${route}`,
        {
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Authorization": `Bearer ${getAuthToken()}`
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
                "Authorization": `Bearer ${getAuthToken()}`
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
                "Authorization": `Bearer ${getAuthToken()}`
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
                "Authorization": `Bearer ${getAuthToken()}`
            }
        }
    )
    return res.data
}

//user methods
export const getUsers = () => apiGetCall("user/get")
export const getCoaches = () => apiGetCall("user/getCoaches")
export const getRegularUsers = () => apiGetCall("user/getUsers")
export const getUserById = (userId: string) => apiGetCall(`user/getById?id=${userId}`)
export const registerUser = (req: RegistrationUserRequest) => apiPostCall<RegistrationUserRequest>("user/register", req)
export const loginUser = async (req: LoginUserRequest): Promise<JwtResponse> => {
    const res: JwtResponse = await apiPostCall<LoginUserRequest>("user/login", req)
    
    setAuthToken(res.jwt)
    setUsername(res.username)
    setRole(res.role)

    return res
}
export const updateUser = (req: UserRequest, userId: string) => apiPutCall<UserRequest>(`user/put?id=${userId}`, req)
export const deleteUser = (userId: string) => apiDeleteCall(`user/delete?id=${userId}`)
export const changeUserPassword = (req: PasswordUserRequest) => apiPutCall<PasswordUserRequest>("user/changePassword", req)

//booking methods
export const getBookings = () => apiGetCall("booking/get")
export const getBookingById = (bookingId: string) => apiGetCall(`booking/getById?id=${bookingId}`)
export const createBooking = (req: BookingRequest) => apiPostCall<BookingRequest>("booking/post", req)
export const updateBooking = (req: BookingRequest, bookingId: string) => apiPutCall<BookingRequest>(`booking/put?=${bookingId}`, req)
export const deleteBooking = (bookingId: string) => apiDeleteCall(`booking/delete?id=${bookingId}`)
export const getTrainerBookings = () => apiGetCall("booking/getTrainerBookings")
export const updateBookingStatus = (bookingId: string, req: UpdateBookingStatusRequest) => apiPutCall<UpdateBookingStatusRequest>(`booking/updateBookingStatus?id=${bookingId}`, req)
export const rejectBooking = (bookingId: string, req: UpdateBookingStatusRequest) => apiPutCall<UpdateBookingStatusRequest>(`booking/rejectBooking?id=${bookingId}`, req)

//training session methods
export const getTrainingSessions = () => apiGetCall("trainingSessions/get")
export const getTrainingSessionById = (trainingSessionId: string) => apiGetCall(`trainingSession/getById?id=${trainingSessionId}`)
export const createTrainingSession = (req: TrainingSessionRequest) => apiPostCall<TrainingSessionRequest>("trainingSession/post", req)
export const updateTrainingSession = (req: TrainingSessionRequest, trainingSessionId: string) => apiPutCall<TrainingSessionRequest>(`trainingSession/put?=${trainingSessionId}`, req)
export const deleteTrainingSession = (trainingsessionId: string) => apiDeleteCall(`trainingSession/delete?id=${trainingsessionId}`)
export const getAvailableTrainingSessions = (date: string) => apiGetCall(`trainingSession/getAvailableTrainingSessionsByDate?date=${date}`)
export const getUserTrainingSessions = () => apiGetCall("trainingSession/getUserTrainingSessions")

//user-authenticated methods (user, trainingSesions and bookings)
export const getMyProfile = () => apiGetCall("user/getMyProfile")
export const updateMyProfile = (req: UserRequest) => apiPutCall<UserRequest>("user/updateMyProfile", req)
export const deleteMyProfile = () => apiDeleteCall("user/deleteMyProfile")
export const getMyTrainingSessions = () => apiGetCall("trainingSession/getMyTrainingSessions")
export const createMyTrainingSession = (req: MyTrainingSessionRequest) => apiPostCall<MyTrainingSessionRequest>("trainingSession/createMyTrainingSession", req)
export const updateMyTrainingSession = (req: MyTrainingSessionRequest, myTrainingSessionId: string) => apiPutCall<MyTrainingSessionRequest>(`trainingSession/updateMyTrainingSession?id=${myTrainingSessionId}`, req)
export const deleteMyTrainingSession = (myTrainingSession: string) => apiDeleteCall(`trainingSession/deleteMyTrainingSession?id=${myTrainingSession}`)
export const getMyBookings = () => apiGetCall("booking/getMyBookings")
export const createMyBooking = (req: MyBookingRequest) => apiPostCall<MyBookingRequest>("booking/createMyBooking", req)
export const updateMyBooking = (req: MyBookingRequest, myBookingId: string) => apiPutCall<MyBookingRequest>(`booking/updateMyBooking?id=${myBookingId}`, req)
export const deleteMyBooking = (myBookingId: string) => apiDeleteCall(`booking/deleteMyBooking?id=${myBookingId}`)

//statistics methods (coach)
export const getNumOfMyTrainingSessions = () => apiGetCall("trainingSession/getNumOfMyTrainingSessions")
export const getNumOfTrainerBookings = () => apiGetCall("booking/getNumOfTrainerBookings")
export const getNumOfPendingTrainerBookings = () => apiGetCall("booking/getNumOfPendingTrainerBookings")
export const getNumOfAcceptedTrainerBookings = () => apiGetCall("booking/getNumOfAcceptedTrainerBookings")
export const getNumOfBeginnerTrainingSessions = () => apiGetCall("trainingSession/getNumOfBeginnerTrainingSessions")
export const getNumOfIntermediateTrainingSessions = () => apiGetCall("trainingSession/getNumOfIntermediateTrainingSessions")
export const getNumOfAdvancedTrainingSessions = () => apiGetCall("trainingSession/getNumOfAdvancedTrainingSessions")

//statistic methods (user)
export const getNumOfMyBookings = () => apiGetCall("booking/getNumOfMyBookings")
export const getNumOfMyUserTrainingSessions = () => apiGetCall("trainingSession/getNumOfMyUserTrainingSessions")

//stripe membership methods (user)
export const createCheckoutSession = (req: CheckoutRequest) => apiPostCall("membership/createCheckoutSession", req)
export const hasActiveMembership = async (date: string): Promise<boolean> => {
    const result = await apiGetCall(`membership/hasActiveMembership?selectedDate=${date}`)
    return !!result
}
export const confirmPayment = (status: string) => apiGetCall(`membership/confirm?status=${status}`)
export const getMyMemberships = () => apiGetCall("membership/getMyMemberships")
export const getMonthOptions = () => apiGetCall("membership/getMonthOptions")

//stripe membership methods (superuser)
export const getMembershipByUser = (userId: string) =>apiGetCall(`membership/getMembershipByUser?userId=${userId}`)
export const getActiveMemberships = () => apiGetCall("membership/getActiveMemberships")
export const getExpiredMemberships = () => apiGetCall("membership/getExpiredMemberships")
export const getNumOfMemberships = () => apiGetCall("membership/getNumOfMemberships")
export const getNumOfActiveMemberships = () => apiGetCall("membership/getNumOfActiveMemberships")
export const getNumOfExpiredMemberships = () => apiGetCall("membership/getNumOfExpiredMemberships")
export const getAllMemberships = () => apiGetCall("membership/getAllMemberships")

//superuser misc methods
export const getUserBySearchTerm = (searchTerm: string) => apiGetCall(`user/getUserBySearchTerm?searchTerm=${searchTerm}`)
export const getCoachBySearchTerm = (searchTerm: string) => apiGetCall(`user/getCoachBySearchTerm?searchTerm=${searchTerm}`)