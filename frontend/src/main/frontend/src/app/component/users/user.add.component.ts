import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {Message} from "primeng/primeng";
import {ActivatedRoute, Router} from "@angular/router";
import {AppUser} from "./user";
import {AppUserDataService} from "./user.data.service";
import {Role} from "../role/role";
import {RoleDataService} from "../role/role.data.service";
import {Report} from "../report/report";

@Component({
  templateUrl: 'user.add.component.html',
  selector: 'users-add',
})
export class AppUsersAddComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  user : AppUser;
  userReports: Report[] = [];

  private params_subscription: any;

  sourceRoles : Role[] = [];

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<AppUser>();
  @Output() onCancelClicked = new EventEmitter();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private userService: AppUserDataService, private roleService : RoleDataService) {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for users-add ' + id);
      if (id === 'add') {
        this.user = new AppUser();

        roleService.findAllRolesWhichDoNotHaveAppUserWithThisId(0).
        subscribe(roles => this.sourceRoles = roles,
          error => this.msgs.push({severity:'error', summary:'Constructor user roles error', detail: error}))

      } else {
        this.userService.getUser(id)
          .subscribe(user => {
              this.user = user;
              this.userReports = user.reports;

              roleService.findAllRolesWhichDoNotHaveAppUserWithThisId(this.user.id).
              subscribe(roles => this.sourceRoles = roles,
                error => this.msgs.push({severity:'error', summary:'Constructor user roles error', detail: error}))

            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onSave() {
    this.userService.update(this.user).
    subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.user);
        } else {
          this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})

        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: 'OK!'})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reports', id]);
  }
}
