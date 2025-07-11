package iGuard.Server.Dto;

public record PlaceCsvRowDto(
        String address,
        Float latitude,
        Float longitude,
        String name
) {

    public static PlaceCsvRowDto from(String[] row) {
        return new PlaceCsvRowDto(
                row[3],
                Float.parseFloat(row[1]),
                Float.parseFloat(row[2]),
                row[0]
        );
    }
}
