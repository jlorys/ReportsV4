import {Role} from "../role/role";
import {Report} from "../report/report";

export class AppUser {
  id: number;
  userName: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: string;
  lastModifiedBy: string;
  roles: Role[];
  reports: Report[];

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.userName = json.userName;
      this.password = json.password;
      this.firstName = json.firstName;
      this.lastName = json.lastName;
      this.email = json.email;
      this.createdDate = json.createdDate;
      this.lastModifiedDate = json.lastModifiedDate;
      this.createdBy = json.createdBy;
      this.lastModifiedBy = json.lastModifiedBy;
      if (json.roles != null) {
        this.roles = Role.toArray(json.roles);
      }
      if (json.reports != null) {
        this.reports = Report.toArray(json.reports);
      }
    }
  }

  // Utils
  static toArray(jsons : any[]) : AppUser[] {
    let users : AppUser[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        users.push(new AppUser(json));
      }
    }
    return users;
  }
}
