import {FieldOfStudy} from "../fieldofstudy/fieldofstudy";
import {Laboratory} from "../laboratory/laboratory";

export class Subject {

  id: number;
  name: string;
  description: string;
  createdDate: string;
  lastModifiedDate: string;
  createdBy: string;
  lastModifiedBy: string;
  laboratories: Laboratory[];
  fieldOfStudy: FieldOfStudy;

  constructor(json? : any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.description;
      this.description = json.description;
      this.createdDate = json.createdDate;
      this.lastModifiedDate = json.lastModifiedDate;
      this.createdBy = json.createdBy;
      this.lastModifiedBy = json.lastModifiedBy;
      if (json.laboratories != null) {
        this.laboratories = Laboratory.toArray(json.laboratories);
      }
      this.fieldOfStudy = json.fieldOfStudy;
    }
  }

  // Utils
  static toArray(jsons : any[]) : Subject[] {
    let subjects : Subject[] = [];
    if (jsons != null) {
      for (let json of jsons) {
        subjects.push(new Subject(json));
      }
    }
    return subjects;
  }
}
