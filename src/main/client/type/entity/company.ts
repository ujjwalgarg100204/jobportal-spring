import { Address } from "./address";

export type Company = {
    id?: number;
    name: string;
    hasLogo: boolean;
    address?: Address;
};

export type GetCompanyOfCurrentRecruiterResponse = Omit<Company, "id"> & {
    id: Required<Company>["id"];
};

export type GetAllCompanyResponse = (Omit<Company, "id"> & {
    id: Required<Company>["id"];
    logoUrl?: string;
})[];

export type CreateNewCompanyRequest = Pick<Company, "name" | "address">;

export type UpdateCompanyRequest = Omit<Company, "hasLogo" | "id"> & {
    id: Required<Company>["id"];
};
