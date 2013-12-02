class MailValidator {
    static boolean evaluate(email) {
        def emailPattern = /[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+((\.|\-)[A-Za-z0-9]+)*(\.[A-Za-z]{2,})/
        return email ==~ emailPattern
    }
}
