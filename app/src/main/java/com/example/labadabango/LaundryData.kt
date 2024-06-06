package com.example.labadabango

import java.util.*
import java.text.NumberFormat
import java.util.*

// Interface to define common properties for laundry data
interface LaundryData {
    val userID: String?
    val countShirts: String?
    val countShorts: String?
    val laundryType: String?
    val radioButtonDeliveryOption: String?
    val inputAddressInformation: String?
    val inputAdditionalAddressInformation: String?
    val spPaymentMethod: String?
    var laundryReferenceNo: String?
    val totalItems: String?
    val etStatus: String?
    val etLaundryDate: String?
    val totalPrice: String?
    val qrCode: String?
    val laundryUserName: String?
}

// Modify DataClassWetLaundry to implement LaundryData interface
data class DataClassWetLaundry(
    val itemCountShirts: String? = null,
    val itemCountShorts: String? = null,
    val itemCountUnderwears: String? = null,
    val itemCountPants: String? = null,
    val itemCountJackets: String? = null,
    val itemCountBedCovers: String? = null,
    val itemCountCarpets: String? = null,

    val countUnderwears: String? = null,
    val countPants: String? = null,
    val countJackets: String? = null,
    val countBedCovers: String? = null,
    val countCarpets: String? = null,
    val spWashInstruction: String? = null,
    val spDryInstruction: String? = null,
    val radioButtonDetergent: String? = null,
    val inputAdditionalDescription: String? = null,

    override val userID: String? = null,
    override val countShirts: String? = null,
    override val countShorts: String? = null,
    override val laundryType: String? = null,
    override val radioButtonDeliveryOption: String? = null,
    override val inputAddressInformation: String? = null,
    override val inputAdditionalAddressInformation: String? = null,
    override val spPaymentMethod: String? = null,
    override var laundryReferenceNo: String? = null,
    override val totalItems: String? = null,
    override val totalPrice: String? = null,
    override val etStatus: String? = null,
    override val etLaundryDate: String? = null,
    override val qrCode: String? = null,
    override val laundryUserName: String? = null,

    ) : LaundryData {

    // No-argument constructor for Firebase deserialization
    constructor() : this(
        null,null,null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null
    )
}

// Modify DataClassDryLaundry to implement LaundryData interface
data class DataClassDryLaundry(
    val itemCountShirts: String? = null,
    val itemCountShorts: String? = null,
    val itemCountGowns: String? = null,
    val itemCountCoats: String? = null,
    val itemCountCurtains: String? = null,
    val itemCountHats: String? = null,
    val itemCountShoes: String? = null,
    val itemCountLeatherBags: String? = null,
    val itemCountBedComforters: String? = null,
    val itemCountStuffedToys: String? = null,
    val itemCountWoolSweaters: String? = null,
    val itemCountDenimJackets: String? = null,
    val itemCountDenimPants: String? = null,
    val itemCountLeatherJackets: String? = null,
    val countGowns: String? = null,
    val countCoats: String? = null,
    val countCurtains: String? = null,
    val countHats: String? = null,
    val countShoes: String? = null,
    val countBedComforters: String? = null,
    val countStuffedToys: String? = null,
    val countWoolSweaters: String? = null,
    val countDenimJackets: String? = null,
    val countLeatherBags: String? = null,
    val countDenimPants: String? = null,
    val countLeatherJackets: String? = null,

    override val userID: String? = null,
    override val countShirts: String? = null,
    override val countShorts: String? = null,
    override val laundryType: String? = null,
    override val radioButtonDeliveryOption: String? = null,
    override val inputAddressInformation: String? = null,
    override val inputAdditionalAddressInformation: String? = null,
    override val spPaymentMethod: String? = null,
    override var laundryReferenceNo: String? = null,
    override val totalItems: String? = null,
    override val totalPrice: String? = null,
    override val etStatus: String? = null,
    override val etLaundryDate: String? = null,
    override val qrCode: String? = null,
    override val laundryUserName: String? = null,

) : LaundryData {
    // No-argument constructor for Firebase deserialization
    constructor() : this(
        null,null,null,null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null
    )
}

// Modify DataClassPremiumLaundry to implement LaundryData interface
data class DataClassPremiumLaundry(
    val itemCountShirts: String? = null,
    val itemCountShorts: String? = null,
    val itemCountUnderwears: String? = null,
    val itemCountPants: String? = null,
    val itemCountJackets: String? = null,
    val itemCountBedCovers: String? = null,
    val itemCountCarpets: String? = null,

    val countUnderwears: String? = null,
    val countPants: String? = null,
    val countJackets: String? = null,
    val countBedCovers: String? = null,
    val countCarpets: String? = null,
    val spWashInstruction: String? = null,
    val spDryInstruction: String? = null,
    val radioButtonDetergent: String? = null,
    val inputAdditionalDescription: String? = null,

    override val userID: String? = null,
    override val countShirts: String? = null,
    override val countShorts: String? = null,
    override val laundryType: String? = null,
    override val radioButtonDeliveryOption: String? = null,
    override val inputAddressInformation: String? = null,
    override val inputAdditionalAddressInformation: String? = null,
    override val spPaymentMethod: String? = null,
    override var laundryReferenceNo: String? = null,
    override val totalItems: String? = null,
    override val totalPrice: String? = null,
    override val etStatus: String? = null,
    override val etLaundryDate: String? = null,
    override val qrCode: String? = null,
    override val laundryUserName: String? = null,

    ) : LaundryData {

    // No-argument constructor for Firebase deserialization
    constructor() : this(
        null,null,null,null,null,null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null
    )
}

// Modify DataClassIronLaundry to implement LaundryData interface
data class DataClassIronLaundry(
    val itemCountShirts: String? = null,
    val itemCountShorts: String? = null,
    val itemCountGowns: String? = null,
    val itemCountCoats: String? = null,
    val itemCountCurtains: String? = null,
    val itemCountHats: String? = null,
    val itemCountShoes: String? = null,
    val itemCountLeatherBags: String? = null,
    val itemCountBedComforters: String? = null,
    val itemCountStuffedToys: String? = null,
    val itemCountWoolSweaters: String? = null,
    val itemCountDenimJackets: String? = null,
    val itemCountDenimPants: String? = null,
    val itemCountLeatherJackets: String? = null,
    val countGowns: String? = null,
    val countCoats: String? = null,
    val countCurtains: String? = null,
    val countHats: String? = null,
    val countShoes: String? = null,
    val countBedComforters: String? = null,
    val countStuffedToys: String? = null,
    val countWoolSweaters: String? = null,
    val countDenimJackets: String? = null,
    val countLeatherBags: String? = null,
    val countDenimPants: String? = null,
    val countLeatherJackets: String? = null,

    override val userID: String? = null,
    override val countShirts: String? = null,
    override val countShorts: String? = null,
    override val laundryType: String? = null,
    override val radioButtonDeliveryOption: String? = null,
    override val inputAddressInformation: String? = null,
    override val inputAdditionalAddressInformation: String? = null,
    override val spPaymentMethod: String? = null,
    override var laundryReferenceNo: String? = null,
    override val totalItems: String? = null,
    override val totalPrice: String? = null,
    override val etStatus: String? = null,
    override val etLaundryDate: String? = null,
    override val qrCode: String? = null,
    override val laundryUserName: String? = null,

    ) : LaundryData {
    // No-argument constructor for Firebase deserialization
    constructor() : this(
        null,null,null,null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null
    )
}
