package iGuard.Server.Dto;

public record ShelterCsvRowDto(
        String facilityType,
        String shelterName,
        String address,
        boolean isAvailable,
        double area,
        int capacity,
        int hasFan,
        int hasAirConditioner,
        boolean isOpenAtNight,
        boolean isOpenOnHolidays,
        boolean allowsAccommodation,
        String notes,
        String managementAgency,
        String managementAgencyPhone,
        String facilityTypeName,
        float latitude,
        float longitude) {

    public static ShelterCsvRowDto from(String[] row) {
        return new ShelterCsvRowDto(
                row[0],
                row[1],
                row[2],
                "y".equalsIgnoreCase(row[3]),
                Double.parseDouble(row[4]),
                Integer.parseInt(row[5]),
                Integer.parseInt(row[6]),
                Integer.parseInt(row[7]),
                "y".equalsIgnoreCase(row[8]),
                "y".equalsIgnoreCase(row[9]),
                "y".equalsIgnoreCase(row[10]),
                row[11],
                row[12],
                row[13],
                row[14],
                Float.parseFloat(row[15]),
                Float.parseFloat(row[16])
        );
    }

}
