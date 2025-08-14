export interface MyTrainingSessionRequest {
    trainingType: string,
    trainingLevel: string,
    beginningOfSession: Date | string,
    endOfSession: Date | string
}