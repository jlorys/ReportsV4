import {Component} from "@angular/core";
import {FieldOfStudyAddComponent} from "../../component.admin/fieldofstudy/fieldofstudy.add.component";

@Component({
  selector: 'reviewerFieldsOfStudies-add',
  templateUrl: '../../component.admin/fieldofstudy/fieldofstudy.add.component.html',
})
export class ReviewerFieldOfStudyAddComponent extends FieldOfStudyAddComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerSubjects', id]);
  }
}
