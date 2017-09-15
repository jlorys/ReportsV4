export class Role {

  id : number;
  roleName : string;
  description : string;

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.roleName = json.roleName;
      this.description = json.description;
    }
  }

  static toArray(jsons : any[]) : Role[] {
    let roles : Role[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        roles.push(new Role(json));
      }
    }
    return roles;
  }
}
